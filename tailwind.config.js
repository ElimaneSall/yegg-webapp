module.exports = {
  content: [
    "./src/main/webapp/**/*.{html,ts,scss}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#0066b3', // Votre couleur d'accent
      },
      width: {
        'sidebar-collapsed': '70px',
        'sidebar-expanded': '250px',
      }
    },
  },
  plugins: [],
}
