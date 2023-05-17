/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        "dark": "#252527",
        "medium": "#92898A",
        "accent": "#32cc8c",
        "accent-dark": "#146542",
        "light": "#FDFFFC",
      },
      spacing: {
        "76": "19rem",
      }
    },
  },
  plugins: [],
}

