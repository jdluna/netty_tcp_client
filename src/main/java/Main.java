
public class Main {
    public static void main(String[] args) throws Exception {

        NettyClient client = new NettyClient("135.251.166.251", 3333);
        TPKT test_msg = new TPKT(10, new byte[] {1,2,3,4,5,6,7,8,9,0});
        
        client.init();
        Thread.sleep(2000);
        client.getChan().writeAndFlush(test_msg);
        Thread.sleep(2000);

        client.close();
     
        Thread.sleep(2000);

    }
}

