var net = require("net");

var cliente = new net.Socket();

cliente.connect(9000, "localhost", () => {
  console.log("Conectado");
  cliente.write("Hola, servidor! Saludos, el Cliente.");
});

cliente.on("data", (data) => {
  console.log("Recibido: " + data);
});

cliente.on("close", () => {
  console.log("Conexión cerrada");
});
