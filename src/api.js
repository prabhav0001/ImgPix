const BASE_URL = "https://actress-gallery-api-production.up.railway.app/api";

export const api = {
    async getLatestGalleries() {
        const response = await fetch(`${BASE_URL}/ragalahari/latest`);
        if (!response.ok) throw new Error('Failed to fetch latest galleries');
        return response.json();
    },

    async getActressesByLetter(letter) {
        const response = await fetch(`${BASE_URL}/ragalahari/letter/${letter}`);
        if (!response.ok) throw new Error('Failed to fetch actresses');
        return response.json();
    },

    async getActressDetail(actressId) {
        const response = await fetch(`${BASE_URL}/actress/${actressId}`);
        if (!response.ok) throw new Error('Failed to fetch actress detail');
        return response.json();
    },

    async getAlbumPhotos(albumUrl) {
        const response = await fetch(`${BASE_URL}/album/photos?album_url=${encodeURIComponent(albumUrl)}`);
        if (!response.ok) throw new Error('Failed to fetch album photos');
        return response.json();
    },

    async searchActresses(query) {
        const response = await fetch(`${BASE_URL}/search?query=${encodeURIComponent(query)}`);
        if (!response.ok) throw new Error('Failed to search actresses');
        return response.json();
    }
};
