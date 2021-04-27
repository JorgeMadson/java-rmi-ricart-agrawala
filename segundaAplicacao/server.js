const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

app.get('/clientesConectados', (requisicao, resposta) => resposta.json({clientes: clientes.length}));

const PORT = 3000;

let clientes = [];
let dadosEnviados = [];

app.listen(PORT, () => {
    console.log(`Ouvindo em http://localhost:${PORT}
  Mostrados numero de clientes conectados em http://localhost:${PORT}/clientesConectados
  Mostrados dados enviados em http://localhost:${PORT}/dadosEnviados
  Para enviar use o index.html ou faça um post em http://localhost:${PORT}/enviarDados
  `)
})

// 
function manipuladorDeEventos(requisicao, resposta, proximo) {
  const headers = {
    'Content-Type': 'text/event-stream',
    'Connection': 'keep-alive',
    'Cache-Control': 'no-cache'
  };
  resposta.writeHead(200, headers);

  const dado = `dados: ${JSON.stringify(dadosEnviados)}\n\n`;

  resposta.write(dado);

  const clientId = Date.now();

  const newClient = {
    id: clientId,
    resposta
  };

  //Cria um novo cliente quando a conexão é aberta
  clientes.push(newClient);

  //Remove o cliente quando a conexão é fechada
  requisicao.on('close', () => {
    console.log(`${clientId} Conexão fechada`);
    clientes = clientes.filter(client => client.id !== clientId);
  });
}

//endpoint que mostra os eventos
app.get('/dadosEnviados', manipuladorDeEventos);

function mandarEventosParaTodos(novoFato) {
  clientes.forEach(cliente => cliente.resposta.write(`dados: ${JSON.stringify(novoFato)}\n\n`))
}

async function adicionarFatos(requisicao, resposta, next) {
  const novoFato = requisicao.body;
  dadosEnviados.push(novoFato);
  resposta.json(novoFato)
  return mandarEventosParaTodos(novoFato);
}

app.post('/enviarDados', adicionarFatos);