(function () {
  window.Reveal.initialize({
    history: true
  })

  var lightBulbs = document.querySelectorAll('.light-bulb')

  lightBulbs.forEach(function (lightBulb) {
    var isVisible = true

    function render () {
      lightBulb.style.opacity = isVisible ? 1 : 0
    }

    lightBulb.onclick = function () {
      isVisible = !isVisible
      render()
    }

    render()
  })
})()
