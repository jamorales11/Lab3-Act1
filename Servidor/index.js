const { createServer } = require("http");
var net = require("net");

var server = net.createServer((socket) => {
  socket.write("Echo server\r\n");
  socket.pipe(socket);
});



server.listen(9000, () => {
  console.log("El servidor está escuchando en %j", server.address());
});

const handleConnection = (conn) => {
  var remoteAddress = conn.remoteAddress + ":" + conn.remotePort;
  console.log("Un nuevo cliente se ha conectado desde %s", remoteAddress);

  const onConnData = (d) => {
    console.log("Datos de conexión desde %s: %j", remoteAddress, d);
    conn.write(d);
  };

  const onConnClose = () => {
    console.log("La conexión desded %s fue cerrada", remoteAddress);
  };

  const onConnError = (err) => {
    console.log("La conexión %s tuvo un error: %s", remoteAddress, err.message);
  };

  conn.on("data", onConnData);
  conn.once("close", onConnClose);
  conn.on("error", onConnError);
};

server.on("connection", handleConnection);
