import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function () {
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
