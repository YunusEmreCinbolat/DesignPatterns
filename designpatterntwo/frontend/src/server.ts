import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { join, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';

// __dirname muadili (ESM)
const __dirname = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = join(__dirname, '../browser');

const app = express();
const angularApp = new AngularNodeAppEngine();

// Statik dosyalar (index:false; SSR devralacak)
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

// Diğer tüm istekleri Angular ile render et
app.use((req, res, next) => {
  angularApp
    .handle(req)
    .then((response) => (response ? writeResponseToNodeResponse(response, res) : next()))
    .catch(next);
});

// Ana giriş noktasıysa sunucuyu başlat
if (isMainModule(import.meta.url)) {
  const port = Number(process.env['PORT'] ?? 4000);

  const server = app.listen(port, () => {
    // Express listen callback argümansızdır; hata dinleme ayrı yapılır
    console.log(`Node Express server listening on http://localhost:${port}`);
  });

  server.on('error', (error: unknown) => {
    console.error('Server error:', error);
    process.exitCode = 1;
  });
}

// Angular CLI / Functions için handler exportu
export const reqHandler = createNodeRequestHandler(app);
