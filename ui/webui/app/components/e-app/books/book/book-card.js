import Ember from 'ember';
import MixinComponentBase from 'webui/mixins/components/base';
import ENV from 'webui/config/environment';

const {computed, observer} = Ember;

export default Ember.Component.extend(MixinComponentBase, {

  classNames: ['ui card', 'e-app-books-book-card'],

  fileStorageUrl: ENV.APP.fileStorageUrl + 'books/book/cover/'

});
