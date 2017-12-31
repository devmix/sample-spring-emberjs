import Component from '@ember/component';
import ENV from 'webui/config/environment';

export default Component.extend({

  fileStorageUrl: ENV.APP.fileStorageUrl + 'sys/export/',

  isFinished: Ember.computed('cell.row.model.state', function () {
    return this.get('cell.row.model.state') === 'FINISHED';
  }),

  actions: {
    download() {
      const id = this.get('cell.row.model.id');
      window.open(this.get('fileStorageUrl') + id, '_blank');
    }
  }
});
