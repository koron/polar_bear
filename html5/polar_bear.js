// vim:set ts=8 sts=2 sw=2 tw=0:

window.onload = function()
{
  setInterval(updateBear, 600);
}

var ids = [ '#bear1', '#bear2', '#bear3', '#bear4', '#bear5' ];
var shown = null;

var lastIndex = -1;
var lastReverse = false;

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
