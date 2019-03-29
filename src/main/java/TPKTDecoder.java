import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TPKTDecoder extends ByteToMessageDecoder {

    public final int BASE_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        System.out.println("[TPKTDecoder] decode");
        if (buffer.readableBytes() >= BASE_LENGTH) {
            System.out.println("[TPKTDecoder] readable buffer length is : "+ buffer.readableBytes());

            if (buffer.readableBytes() > 2048) {
                buffer.skipBytes(buffer.readableBytes());
            }

            int beginReader = buffer.readerIndex();
            System.out.println("[TPKTDecoder] beginReader is : "+ beginReader);

            int length = buffer.readInt();
            System.out.println("[TPKTDecoder] TPKT length is : "+ length);
          
            if (buffer.readableBytes() < length) {
                System.out.println("[TPKTDecoder] TPKT length is not enough");
                buffer.readerIndex(beginReader);
                return;
            }

            byte[] data = new byte[length];
            buffer.readBytes(data);

            TPKT protocol = new TPKT(data.length, data);
            System.out.println("[TPKTDecoder] decode, msg is : "+ protocol.toString());
            out.add(protocol);
        }
    }

}
