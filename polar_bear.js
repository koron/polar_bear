// vim:set ts=8 sts=2 sw=2 tw=0:

window.onload = function()
{
  start();
  muteButton = document.getElementById('control_mute');
  muteButton.onclick = function () { toggleSound(); }
}

var imgIds = [ 'bear1', 'bear2', 'bear3', 'bear4', 'bear5' ];
var audioIds = [ 'se1', 'se2', 'se3', 'se4', 'se5' ];

var intervalId = undefined;
var shown = null;
var lastIndex = -1;
var lastReverse = false;
var soundFrame = 0;
var soundEnabled = true;
var muteButton = undefined;

function start()
{
  if (!intervalId) {
    intervalId = setInterval(updateBear, 600);
  }
}

function updateBear()
{
  var index = lastIndex;
  var reverse = lastReverse;
  while (index == lastIndex && reverse == lastReverse) {
    index = Math.floor(Math.random() * imgIds.length);
    reverse = Math.random() < 0.5;
  }
  lastIndex = index;
  lastReverse = reverse;

  var soundIndex = 0;
  var soundMax = audioIds.length - 1;
  soundFrame = (soundFrame + 1) % soundMax;
  if (soundFrame == 0) {
    soundIndex = Math.floor(Math.random() * soundMax) + 1;
  }

  if (soundEnabled) {
    playSound(soundIndex);
  }
  showBear(index, reverse);
}

function playSound(index)
{
  var audio = document.getElementById(audioIds[index]);
  audio.play();
}

function showBear(index, flipH)
{
  var id = imgIds[index];
  if (shown != undefined) {
    shown.style.visibility = 'hidden';
  }
  shown = document.getElementById(id);
  shown.style.visibility = 'visible';
  if (flipH) {
    shown.style['-webkit-transform'] = 'scale(-1, 1)';
    shown.style['-moz-transform'] = 'scale(-1, 1)';
  } else {
    shown.style['-webkit-transform'] = 'scale(1, 1)';
    shown.style['-moz-transform'] = 'scale(1, 1)';
  }
}

function toggleSound() {
  soundEnabled = !soundEnabled;
  if (muteButton == undefined) {
    return;
  }
  var nodes = muteButton.getElementsByTagName('img');
  var imgSpeaker = nodes[0];
  var imgMute = nodes[1];
  if (soundEnabled) {
    imgSpeaker.style.visibility = 'visible';
    imgMute.style.visibility = 'hidden';
  } else {
    imgSpeaker.style.visibility = 'hidden';
    imgMute.style.visibility = 'visible';
  }
}
