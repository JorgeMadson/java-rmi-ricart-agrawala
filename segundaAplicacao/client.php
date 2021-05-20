<?php
//Endereço do servidor
$uri = 'http://localhost:3000';
//Preparando a chamada
$ch = curl_init($uri);
//Cabeçalho
curl_setopt_array($ch, array(
    CURLOPT_HTTPHEADER  => array('Cache-Control: no-cache', 'Content-Type: text/event-stream', 'token: 123'),
    CURLOPT_RETURNTRANSFER  =>true,
    CURLOPT_VERBOSE     => 1
));
//Fazendo a chamada
$out = curl_exec($ch);
curl_close($ch);
// echo response output
echo $out;
