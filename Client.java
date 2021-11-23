import java.io.*;
import java.net.Socket;

class Client {
    

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    // private String username;

    public Socket conect(){
        try {
            socket = new Socket("localhost", 3389);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return socket;
    }
    Socket socket=conect();
    public Client() {
        try {

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(int x, int y) {
        try {
            while (socket.isConnected()) {
                bufferedWriter.write(x + ":" + y);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String input;
                    while (socket.isConnected()) {
                        try {
                            input = bufferedReader.readLine();
                            System.out.println(input);
                        } 
                        catch (IOException e) {
                            closeEverything(socket, bufferedReader, bufferedWriter);
                        }
                    }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) throws IOException {

    //     Scanner scanner = new Scanner(System.in);
    //     System.out.print("Enter your username for the group chat: ");
    //     String username = scanner.nextLine();
    //     Socket socket = new Socket("52.183.155.51", 3389);

    //     Client client = new Client(socket);
    //     client.listenForMessage();
    //     client.sendMessage();
    // }
}
