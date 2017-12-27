import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function () {
  this.route('login');

  this.route('storage', function () {
    this.route('books', function () {
      this.route('book', function () {
        this.route('new');
        this.route('edit', {path: 'edit/:id'});
      });
      this.route('author');
      this.route('genre');
      this.route('publisher');
    });
  });
});

export default Router;
