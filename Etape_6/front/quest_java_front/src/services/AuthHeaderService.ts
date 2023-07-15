const AuthHeaderService = {
  header() {
    const jwtStr = localStorage.getItem("token");
    if (jwtStr) {
      return { Authorization: "Bearer " + jwtStr };
    } else {
      return { Authorization: "" };
    }
  },
};
export default AuthHeaderService;