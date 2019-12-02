import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

const val SERVERPORT = 9999;
const val SERVERIP = "127.0.0.1";

fun main() {
    val socket = Socket(SERVERIP, SERVERPORT);
    val input = BufferedReader(InputStreamReader(socket.getInputStream()));
    val inputKeyboard = BufferedReader(InputStreamReader(System.`in`));
    val out = PrintWriter(socket.getOutputStream(), true);
    var inputString = "Hello Server";

    do {
        print("Packet: ")
        inputString = inputKeyboard.readLine();
        out.println(inputString);
        val serverResponse = input.readLine();
        println(serverResponse);
    } while (inputString.length > 0)

    socket.close();

}