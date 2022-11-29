export default function ({ store, redirect }) {
  if (process.client) {
    const localUser = store.state.auth.user;
    const role = localUser ? localUser.authority : "";
    if (role !== "ADMIN") {
      return redirect("/login");
    }
  }
}
