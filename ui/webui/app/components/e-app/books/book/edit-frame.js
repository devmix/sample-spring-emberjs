import Ember from 'ember';
import EditFrameMixin from 'webui/mixins/app/commons/entity/edit-frame'
import ENV from 'webui/config/environment';

export default Ember.Component.extend(EditFrameMixin, {

  fileStorageUrl: ENV.APP.fileStorageUrl + 'storage/books/book/cover/',
  imgVersion: 0,

  actions: {
    uploadImage(file) {
      file.upload(this.fileStorageUrl + this.get('model').get('id')).then((file) => {
        this.incrementProperty('imgVersion');
      });
    }
  }
});
