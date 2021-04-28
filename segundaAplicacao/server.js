const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

app.get('/peersConectados', (requisicao, resposta) => resposta.json({peers: peers.length}));

const PORTA = 3000;

let peers = [];
let dadosEnviados = [];

app.listen(PORTA, () => {
    console.log(`Ouvindo em http://localhost:${PORTA}
  Mostrados numero de peers conectados em http://localhost:${PORTA}/peersConectados
  Mostrados dados enviados em http://localhost:${PORTA}/dadosEnviados
  Para enviar use o index.html ou faça um post em http://localhost:${PORTA}/enviarDados
  `);
});

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

  const peerId = Date.now();

  const novopeer = {
    id: peerId,
    resposta
  };

  //Cria um novo peer quando a conexão é aberta
  peers.push(novopeer);

  //Remove o peer quando a conexão é fechada
  requisicao.on('close', () => {
    console.log(`${peerId} Conexão fechada`);
    peers = peers.filter(client => client.id !== peerId);
  });
}

//endpoint que mostra os eventos
app.get('/dadosEnviados', manipuladorDeEventos);

function mandarEventosParaTodos(novoFato) {
  peers.forEach(peer => peer.resposta.write(`dados: ${JSON.stringify(novoFato)}\n\n`));
}

async function adicionarDados(requisicao, resposta, proximo) {
  const novoFato = requisicao.body;
  dadosEnviados.push(novoFato);
  resposta.json(novoFato);
  return mandarEventosParaTodos(novoFato);
}

app.post('/enviarDados', adicionarDados);