export default function({ store, redirect }) {
    const localUser = store.state.auth.user;
    const role = localUser ? localUser.authority : "";
    if (role !== "ADMIN" && role !== "USER" && role !== "MANAGER") {
        return redirect("/login");
    }
}