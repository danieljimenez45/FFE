const modalTrailer = document.getElementById('modalTrailer');
const trailerIframe = document.getElementById('trailerIframe');

if (modalTrailer && trailerIframe) {
    const trailerSrc = trailerIframe.src;

    modalTrailer.addEventListener('hidden.bs.modal', function () {
        trailerIframe.src = "";
        trailerIframe.src = trailerSrc;
    });
}