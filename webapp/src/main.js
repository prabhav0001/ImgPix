import './style.css';
import { api } from './api.js';
import { UI } from './ui.js';

const app = document.querySelector('#app');
const content = UI.renderHeader(app, handleSearch, loadHome);

// State
let currentActress = null;

// Initial Load
loadHome();

async function loadHome() {
    UI.showLoading(content);
    try {
        const data = await api.getLatestGalleries();
        UI.renderGallery(content, data, handleActressClick);
    } catch (error) {
        console.error(error);
        content.innerHTML = '<div class="loading">Error loading content</div>';
    }
}

async function handleSearch(query) {
    UI.showLoading(content);
    try {
        const data = await api.searchActresses(query);
        UI.renderGallery(content, data, handleActressClick);
    } catch (error) {
        console.error(error);
        content.innerHTML = '<div class="loading">Error searching</div>';
    }
}

async function handleActressClick(actress) {
    UI.showLoading(content);
    try {
        const detail = await api.getActressDetail(actress.id);
        currentActress = detail;
        UI.renderActressDetail(content, detail, handleAlbumClick, loadHome);
    } catch (error) {
        console.error(error);
        content.innerHTML = '<div class="loading">Error loading details</div>';
    }
}

async function handleAlbumClick(album) {
    UI.showLoading(content);
    try {
        const photos = await api.getAlbumPhotos(album.url);
        UI.renderAlbumPhotos(content, photos, () => handleActressClick(currentActress));
    } catch (error) {
        console.error(error);
        content.innerHTML = '<div class="loading">Error loading photos</div>';
    }
}

