import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
public class TPKTEncoder extends MessageToByteEncoder<TPKT> {
    @Override
    protected void encode(ChannelHandlerContext tcx, TPKT msg, ByteBuf out) throws Exception {
        System.out.println("[TPKTEncoder] encode. msg is : "+ msg.toString());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
    }
}
