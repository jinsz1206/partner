import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from '@vant/auto-import-resolver';



export default {
  plugins: [vue(),
    AutoImport({
      resolvers: [VantResolver()],
    }),
    Components({
      resolvers: [VantResolver()],
    }),
  ],
  // 设置服务器代理到后端
  // react项目里在后端没设置CORS都可以?这里需要在后端设置代理服务器接口
  server: {
    proxy: {
      '/api/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
};
