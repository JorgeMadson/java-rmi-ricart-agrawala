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
let fatos = [];

app.listen(PORT, () => {
  console.log(`Serviço de Fatos de eventos ouvindo em http://localhost:${PORT}`)
})

// 
function eventsHandler(requisicao, resposta, proximo) {
  const headers = {
    'Content-Type': 'text/event-stream',
    'Connection': 'keep-alive',
    'Cache-Control': 'no-cache'
  };
  resposta.writeHead(200, headers);

  const dado = `dados: ${JSON.stringify(fatos)}\n\n`;

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
    console.log(`${clientId} Coneção fechada`);
    clientes = clientes.filter(client => client.id !== clientId);
  });
}

app.get('/eventos', eventsHandler);