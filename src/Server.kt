import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.util.concurrent.*
import java.util.TimeZone


const val PORT = 9999
const val TIMEZONE = 7
fun main() {
    val listener = ServerSocket(PORT)
    println("[SERVER] start")


    val clients = ArrayList<ClientHandler>()
    val pool = Executors.newFixedThreadPool(10)
    while (true) {
        val client = listener.accept()
        //val id = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val clientThread = ClientHandler(client)
        clients.add(clientThread)
        for (c in clients) {
            if (c.client.isClosed) {
                println("client closed " + c.clientId);
                clients.remove(c);
            }

        }
        println("clients.size " + clients.size)
        pool.execute(clientThread)
    }
}

class ClientHandler : Runnable {
    var client: Socket
    var clientIn: BufferedReader
    var clientOut: PrintWriter
    var clientId: String = ""
    var request = ""

    constructor(clientSocket: Socket) {
        println("New Client Connected");
        //  this.clientId = clientId;
        this.client = clientSocket
        this.clientIn = BufferedReader(InputStreamReader(client.getInputStream()))
        this.clientOut = PrintWriter(client.getOutputStream(), true)
        // clientOut.println("[SERVER] your client id: " + clientId)
    }

    override fun run() {


//        do {
//            try {
//                if (!client.isClosed) {
//                    request = clientIn.readLine()
//                    val header = request.substring(0, 2)
//                    if (header.equals("IW")) {
//                        commandWord(request.substring(2, 6), request.substring(6, request.length))
//
//                        //clientOut.println("[SERVER] Client " + clientId + " said: " + request)
//                        println("[CLIENT] " + request)
//                    }
//                }
//            } catch (ex: IOException) {
//                clientOut.close()
//                clientIn.close()
//            }
//        } while (true)
        try {
            while (true) {
                request = clientIn.readLine()
                val header = request.substring(0, 2)
                if (header.equals("IW")) {
                    commandWord(request.substring(2, 6), request.substring(6, request.length))

                    //clientOut.println("[SERVER] Client " + clientId + " said: " + request)
                    println("[CLIENT] " + request)
                }
            }
        } catch (ex: Exception) {
            clientOut.close()
            clientIn.close()
            client.close()
        }


    }

    fun commandWord(command: String, param: String) {
        println(command + " " + param)
        when (command) {
            "AP00" -> {
                val timeZone = TimeZone.getTimeZone("UTC")
                val cal = Calendar.getInstance(timeZone)
                this.clientId = param.replace(
                    "#",
                    ""
                )

                cal.set(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.HOUR_OF_DAY) - TIMEZONE,
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND)
                )
                clientOut.println(
                    "[SERVER RESPONSE] IMEI-" + clientId + ": " + "IWBP00," + SimpleDateFormat("yyyyMMddHHmmss").format(
                        cal.time
                    ) + "," + TIMEZONE
                )
            }
            "AP01" -> {
                clientOut.println(
                    "[SERVER RESPONSE] IMEI-" + clientId + ": " + param
                )
            }
            "AP02" -> {

            }
            "AP03" -> {

            }
            "AP07" -> {

            }
            "AP10" -> {

            }
            "AP49" -> {

            }
            "APHT" -> {

            }
        }
//        switch (command) {
//
//        }
    }
}

