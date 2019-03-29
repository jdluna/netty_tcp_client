import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private volatile EventLoopGroup workerGroup;

    private volatile Bootstrap bootstrap;

    private volatile boolean closed = false;

    private final String remoteHost;

    private final int remotePort;
    private Channel chan;

    public NettyClient(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public Channel getChan()
    {
        return chan;
    }

    public void close() {
        closed = true;
        workerGroup.shutdownGracefully();
        System.out.println("Stopped Tcp Client: " + getServerInfo());
    }

    void onRead(Object msg)
    {
        System.out.println("[NettyClient]Received Message : " + msg);
    }

    public void init() {
        closed = false;

        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("encoder", new TPKTEncoder());
                pipeline.addLast("decoder", new TPKTDecoder());
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        ctx.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                    }
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("in channelActive");
                    }
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        //System.out.println("Received Message : " + msg);
                        //ctx.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                        onRead(msg);
                    }
                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("channelReadComplete");
                    }
                });
            }
        });

        doConnect();
    }

    private void doConnect() {
        if (closed) {
            return;
        }

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort));
        chan = future.channel();
        System.out.println("[doConnect] chan is : " + chan);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("Started Tcp Client: " + getServerInfo());
                } else {
                    System.out.println("Started Tcp Client Failed: " + getServerInfo());

                    f.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    private String getServerInfo() {
        return String.format("RemoteHost=%s RemotePort=%d", remotePort, remotePort);
    }
}
