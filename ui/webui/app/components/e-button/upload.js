import Component from '@ember/component';

export default Component.extend({
  tagName: 'span',

  accept: null,
  multiple: null,
  disabled: false,
  queueId: 'file-upload-button-' + new Date().getTime(),
  fileQueue: Ember.inject.service(),

  didReceiveAttrs() {
    const queue = this.get('queue');
    if (queue) {
      const properties = this.getProperties('accept', 'multiple', 'disabled');
      properties.onfileadd = this._onFileAdd.bind(this);
      Ember.setProperties(queue, properties);
    }
  },

  queue: Ember.computed('queueId', {
    get() {
      const queueName = this.get('queueId');
      if (queueName != null) {
        const queues = this.get('fileQueue');
        return queues.find(queueName) || queues.create(queueName);
      }
    }
  }),

  _onFileAdd(file) {
    file.upload(this.get('uploadUrl')).then(
      (file) => this.sendAction('action', true, file),
      () => this.sendAction('action', false));
  },

  actions: {
    change(files) {
      this.get('queue')._addFiles(files, 'browse');
      this.$().children('input').val(null);
    },

    onClick() {
      this.$().children('input').click();
    }
  }
});
