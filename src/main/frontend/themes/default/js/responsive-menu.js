// Responsive menu behavior
window.addEventListener("DOMContentLoaded", () => {
  // Function to check if we're on mobile
  function isMobile() {
    return window.innerWidth <= 768
  }

  // Function to handle drawer toggle behavior
  function setupSideMenu() {
    const drawerToggle = document.querySelector("vaadin-drawer-toggle")
    if (!drawerToggle) return

    // Make sure side menu is hidden by default
    const sideMenu = document.querySelector(".side-menu-component")
    if (sideMenu) {
      sideMenu.style.transform = "translateX(-100%)"
      sideMenu.style.transition = "transform 0.3s ease"
    }

    // Close side menu when clicking outside
    document.addEventListener("click", (event) => {
      if (
        isMobile() &&
        sideMenu &&
        sideMenu.style.transform === "translateX(0px)" &&
        !sideMenu.contains(event.target) &&
        !drawerToggle.contains(event.target)
      ) {
        sideMenu.style.transform = "translateX(-100%)"
      }
    })
  }

  // Initial setup
  setupSideMenu()

  // Handle route changes
  window.addEventListener("vaadin-router-location-changed", () => {
    const sideMenu = document.querySelector(".side-menu-component")
    if (sideMenu && sideMenu.style.transform === "translateX(0px)") {
      sideMenu.style.transform = "translateX(-100%)"
    }
  })
})
