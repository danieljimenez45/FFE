
document.addEventListener('DOMContentLoaded', () => {

    const buscador = document.getElementById('buscadorPeliculas');

    const peliculas = document.querySelectorAll(".pelicula-item");

    const mensajesSinResultados = document.getElementById("sin-resultados-peliculas");

    if( buscador && peliculas.length > 0){

        buscador.addEventListener("input", () => {

            const textoBuscado = buscador.value.toLowerCase().trim();

            let peliculasVisibles = 0;

            peliculas.forEach((pelicula) => {

                const titulo = pelicula.dataset.titulo;

                if(titulo.includes(textoBuscado)){
                    pelicula.classList.remove('d-none');

                    peliculasVisibles++;
                }else{
                    pelicula.classList.add('d-none');
                }
            });

            if (mensajesSinResultados) {

                mensajesSinResultados.classList.toggle('d-none', peliculasVisibles > 0);
            }
        });
    }
});