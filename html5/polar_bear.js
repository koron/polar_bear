// vim:set ts=8 sts=2 sw=2 tw=0:

window.onload = function()
{
  //prepareAudio();
  start();
}

var ids = [ '#bear1', '#bear2', '#bear3', '#bear4', '#bear5' ];
var audios = [
  new Audio('sounds/se_clock.ogg'),
  new Audio('sounds/se_kah.ogg'),
  new Audio('sounds/se_karan.ogg'),
  new Audio('sounds/se_kasha.ogg'),
  new Audio('sounds/se_powa.ogg')
];

var intervalId = undefined;
var shown = null;
var lastIndex = -1;
var lastReverse = false;
var soundFrame = 0;

function prepareAudio() {
  var count = 0;
  for (var i = 0; i < audios.length; ++i) {
    var audio = audios[i];
    console.log('#'+i + ' ' +audio);
    audio.onload = function() {
      console.log(audio.src);
      ++count;
      if (count >= audios.length) {
	start();
      }
    };
  }
}
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
    index = Math.floor(Math.random() * ids.length);
    reverse = Math.random() < 0.5;
  }
  lastIndex = index;
  lastReverse = reverse;

  var soundIndex = 0;
  var soundMax = audios.length - 1;
  soundFrame = (soundFrame + 1) % soundMax;
  if (soundFrame == 0) {
    soundIndex = Math.floor(Math.random() * soundMax) + 1;
  }

  var audio = audios[soundIndex];
  audio.play();
  showBear(index, reverse);
}

function showBear(index, flipH)
{
  var id = ids[index];
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
