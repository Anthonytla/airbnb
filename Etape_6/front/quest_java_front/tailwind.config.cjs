/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}", "./node_modules/flowbite/**/*.js"],
  theme: {
    extend: {
      colors: {
        "grey-transparent": "hsl(217 8% 33% / 0.6)",
      },
      backgroundImage: {
        'host-pattern': "url('./assets/location2.jpg')",
        'traveler-pattern': "url('./assets/plane.jpg')",
      }
    },
  },
  plugins: [require("daisyui")],
};
