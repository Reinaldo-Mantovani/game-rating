// Custom element for the game carousel
class GameCarousel extends HTMLElement {
  constructor() {
    super()
    this.autoPlay = false
    this.autoPlayDelay = 5000
    this.currentSlide = 0
  }

  connectedCallback() {
    // Initialize the carousel when connected to DOM
    this.initCarousel()

    // Create navigation buttons
    this.createNavButtons()
  }

  initCarousel() {
    const slides = this.querySelectorAll(".carousel-item")
    if (slides.length === 0) return

    // Hide all slides except the first one
    slides.forEach((slide, index) => {
      slide.style.display = index === 0 ? "block" : "none"
      slide.style.transition = "opacity 0.5s ease"
    })

    // Set up auto-play if enabled
    if (this.autoPlay && this.autoPlayDelay > 0) {
      this.startAutoPlay()
    }
  }

  createNavButtons() {
    // Create previous button
    const prevBtn = document.createElement("button")
    prevBtn.innerHTML = "❮"
    prevBtn.className = "carousel-nav prev"
    prevBtn.addEventListener("click", () => this.showSlide(this.currentSlide - 1))

    // Create next button
    const nextBtn = document.createElement("button")
    nextBtn.innerHTML = "❯"
    nextBtn.className = "carousel-nav next"
    nextBtn.addEventListener("click", () => this.showSlide(this.currentSlide + 1))

    // Add buttons to carousel
    this.appendChild(prevBtn)
    this.appendChild(nextBtn)

    // Add styles for buttons
    const style = document.createElement("style")
    style.textContent = `
      .carousel-nav {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        background: rgba(0, 0, 0, 0.5);
        color: white;
        border: none;
        border-radius: 50%;
        width: 40px;
        height: 40px;
        font-size: 18px;
        cursor: pointer;
        z-index: 10;
        opacity: 0.7;
        transition: opacity 0.3s ease;
      }
      .carousel-nav:hover {
        opacity: 1;
      }
      .carousel-nav.prev {
        left: 10px;
      }
      .carousel-nav.next {
        right: 10px;
      }
    `
    this.appendChild(style)
  }

  showSlide(index) {
    const slides = this.querySelectorAll(".carousel-item")
    if (slides.length === 0) return

    // Handle index bounds
    if (index < 0) index = slides.length - 1
    if (index >= slides.length) index = 0

    // Hide current slide and show new slide
    slides[this.currentSlide].style.display = "none"
    slides[index].style.display = "block"

    // Update current slide index
    this.currentSlide = index
  }

  startAutoPlay() {
    this.autoPlayInterval = setInterval(() => {
      this.showSlide(this.currentSlide + 1)
    }, this.autoPlayDelay)
  }

  stopAutoPlay() {
    if (this.autoPlayInterval) {
      clearInterval(this.autoPlayInterval)
    }
  }

  // Clean up when element is removed
  disconnectedCallback() {
    this.stopAutoPlay()
  }
}

// Register the custom element
customElements.define("game-carousel", GameCarousel)
