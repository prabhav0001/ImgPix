export const UI = {
    renderHeader(container, onSearch, onHome) {
        container.innerHTML = `
      <header>
        <h1 id="logo">Walls</h1>
        <div class="search-container">
          <input type="text" class="search-input" placeholder="Search actresses..." id="search-input">
        </div>
      </header>
      <main id="content"></main>
    `;

        document.getElementById('logo').addEventListener('click', onHome);

        const searchInput = document.getElementById('search-input');
        let debounceTimer;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                const query = e.target.value.trim();
                if (query) onSearch(query);
                else onHome();
            }, 500);
        });

        return document.getElementById('content');
    },

    renderGallery(container, items, onItemClick) {
        if (items.length === 0) {
            container.innerHTML = '<div class="loading">No results found</div>';
            return;
        }

        const grid = document.createElement('div');
        grid.className = 'gallery-grid';

        items.forEach(item => {
            const card = document.createElement('div');
            card.className = 'card';
            card.innerHTML = `
        <img src="${item.thumbnail || 'placeholder.jpg'}" class="card-image" loading="lazy" alt="${item.name}">
        <div class="card-content">
          <h3 class="card-title">${item.name}</h3>
          ${item.profession ? `<div class="card-subtitle">${item.profession}</div>` : ''}
        </div>
      `;
            card.addEventListener('click', () => onItemClick(item));
            grid.appendChild(card);
        });

        container.innerHTML = '';
        container.appendChild(grid);
    },

    renderActressDetail(container, detail, onAlbumClick, onBack) {
        const html = `
      <div class="detail-view">
        <button class="back-btn">← Back</button>
        
        <div class="detail-header">
          <img src="${detail.images[0]}" class="detail-image" alt="${detail.name}">
          <div class="detail-info">
            <h2>${detail.name}</h2>
            <div class="tags">
              ${detail.profession ? `<span class="tag">${detail.profession}</span>` : ''}
              ${detail.nationality ? `<span class="tag">${detail.nationality}</span>` : ''}
              ${detail.age ? `<span class="tag">${detail.age} years</span>` : ''}
            </div>
            ${detail.bio ? `<p>${detail.bio}</p>` : ''}
          </div>
        </div>

        <h3>Albums</h3>
        <div class="gallery-grid">
          ${detail.albums.map((album, index) => `
            <div class="card album-card" data-index="${index}">
              <img src="${album.thumbnail}" class="card-image" loading="lazy" alt="${album.name}">
              <div class="card-content">
                <h3 class="card-title">${album.name}</h3>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
    `;

        container.innerHTML = html;

        container.querySelector('.back-btn').addEventListener('click', onBack);

        container.querySelectorAll('.album-card').forEach(card => {
            card.addEventListener('click', () => {
                const index = card.dataset.index;
                onAlbumClick(detail.albums[index]);
            });
        });
    },

    renderAlbumPhotos(container, photos, onBack) {
        const html = `
      <div class="detail-view">
        <button class="back-btn">← Back to Actress</button>
        <div class="photos-grid">
          ${photos.map(url => `
            <div class="photo-item">
              <img src="${url}" loading="lazy" alt="Photo">
            </div>
          `).join('')}
        </div>
      </div>
    `;

        container.innerHTML = html;
        container.querySelector('.back-btn').addEventListener('click', onBack);
    },

    showLoading(container) {
        container.innerHTML = '<div class="loading">Loading...</div>';
    }
};
