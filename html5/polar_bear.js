// vim:set ts=8 sts=2 sw=2 tw=0:

window.onload = function()
{
  start();
}

var imgIds = [ '#bear1', '#bear2', '#bear3', '#bear4', '#bear5' ];
var audioIds = [ 'se1', 'se2', 'se3', 'se4', 'se5' ];

var intervalId = undefined;
var shown = null;
var lastIndex = -1;
var lastReverse = false;
var soundFrame = 0;

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

  playSound(soundIndex);
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
  if (shown != null) {
    shown.css('visibility', 'hidden');
  }
  shown = $(id);
  shown.css('visibility', 'visible');
  if (flipH) {
    shown.css('-webkit-transform', 'scale(-1, 1)')
    shown.css('-moz-transform', 'scale(-1, 1)')
  } else {
    shown.css('-webkit-transform', 'scale(1, 1)')
    shown.css('-moz-transform', 'scale(1, 1)')
  }
}
