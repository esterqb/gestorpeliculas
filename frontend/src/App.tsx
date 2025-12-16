import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { UserProvider } from './context/UserContext';
import ProfileSelectionPage from './pages/ProfileSelectionPage';
import HomePage from './pages/HomePage';
import MovieDetails from './pages/MovieDetails';
import Header from './components/Header';
import MyListPage from './pages/MyListPage';

const mainContentStyle: React.CSSProperties = {
    paddingTop: '88px',
    boxSizing: 'border-box',
};

function App() {
    return (
        <Router>
            <UserProvider>
                <Routes>
                    {/* 1. / (ROOT): Selección de Perfil */}
                    <Route path="/" element={<ProfileSelectionPage />} />

                    {/* 2. /inicio (INICIO): Catálogo de películas CON BANNER */}
                    {/* */}
                    <Route
                        path="/inicio"
                        element={<> <Header /> <main style={mainContentStyle}> <HomePage showBanner={true} viewType="home" /> </main> </>}
                    />

                    {/* 3. /peliculas (CATÁLOGO/GÉNEROS): Catalog View (SIN BANNER, SOLO CATEGORÍAS) */}
                    {/**/}
                    <Route
                        path="/peliculas"
                        element={<> <Header /> <main style={mainContentStyle}> <HomePage showBanner={false} viewType="catalog" /> </main> </>}
                    />

                    {/* 4. /list (MI LISTA) */}
                    <Route
                        path="/list"
                        element={<> <Header /> <main style={mainContentStyle}> <MyListPage /> </main> </>}
                    />

                    {/* 5. /peliculas/:id (DETALLES) */}
                    <Route
                        path="/peliculas/:id"
                        element={<> <Header /> <main style={mainContentStyle}> <MovieDetails /> </main> </>}
                    />
                </Routes>
            </UserProvider>
        </Router>
    );
}

export default App;