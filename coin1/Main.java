package coin1;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;

public class Main {
    private static Socket sock = null;
    private static OutputStream out = null;
    private static InputStream in = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;

    public static void init() {
        try {
            sock = new Socket("pwnable.kr", 9007);
            out = sock.getOutputStream();
            in = sock.getInputStream();
            bw = new BufferedWriter(new OutputStreamWriter(out));
            br = new BufferedReader(new InputStreamReader(in));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int findAnswer() {
        try {
            // 서버로부터 메시지를 전달 받습니다.
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.length() != 0 && line.charAt(0) == 'N')
                    break;
            }
            int N = Integer.parseInt(line.split("N=")[1].split(" C")[0]);
            int C = Integer.parseInt(line.split(" C=")[1]);
            System.out.println("N의 값은" + N + ", C의 값은 " + C);
            int up = N - 1;
            int down = 0;
            while (down <= up && C > 0) {
                int middle = (up + down) / 2;
                String message = "";
                for (int i = down; i <= middle; i++) {
                    message += i + " ";
                }
                // 서버로 확인하고자 하는 코인들의 번호를 전달합니다.
                System.out.println("보낸 값: " + message);
                bw.write(message);
                bw.newLine();
                bw.flush();
                C--;
                // 서버로부터 결과 정보를 전달 받습니다.
                line = br.readLine();
                System.out.println("받은 값: " + line);
                int next = Integer.parseInt(line);
                // 10으로 나누어진다면 해당 범위에 가짜 코인이 없습니다.
                if (next % 10 == 0) {
                    down = middle + 1;
                }
                // 그렇지 않다면 해당 범위에 가짜 코인이 있습니다.
                else {
                    up = middle - 1;
                }
            }
            // 아직 확인할 수 있는 횟수가 더 남았다면 다 쓸 때까지 서버에 전송합니다/
            if (C > 0) {
                bw.write(down + "");
                bw.newLine();
                bw.flush();
            }
            // 결과적으로 찾은 가짜 코인의 번호는 down에 담겨있습니다.
            return down;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public static void close() {
        try {
            bw.close();
            br.close();
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            init();
            int count = 200;
            while (count-- > 0) {
                int result = findAnswer();
                System.out.println("가짜 코인 발견: " + result);
                bw.write(result + "");
                bw.newLine();
                bw.flush();
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}