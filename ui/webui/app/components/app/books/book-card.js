import Ember from 'ember';
import MixinComponentBase from '../../../mixins/components/base';
import ENV from '../../../config/environment';

const { computed, observer } = Ember;

export default Ember.Component.extend(MixinComponentBase, {

  classNames: ['component-app-books-book-card'],

  fileStorageUrl: (ENV.host || '') + '/api/core/storage/file/get/storage/books/book/cover/',

});
