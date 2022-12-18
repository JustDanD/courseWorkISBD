import {BrowserRouter as Router, Outlet, Route, Routes,} from "react-router-dom";
import {SignIn} from "./signIn/SignIn";
import {SignUp} from "./signUp/SignUp";
import {Home} from "./home/Home";

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Outlet/>}>
              <Route index element={<Home/>}/>
              <Route path="signIn" element={<SignIn/>}/>
              <Route path="signUp" element={<SignUp/>}/>
          </Route>
        </Routes>
      </Router>
  );
}

export default App;
