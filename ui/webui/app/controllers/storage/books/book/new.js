import Ember from 'ember';

export default Ember.Controller.extend({

  fields: undefined,

  actions: {
    registerFields(field, register) {
      this.set('fields', register ? field : undefined);
    },

    commit() {
      this.get('fields').commit().then((model) => {
        this.transitionToRoute('storage.books.book.edit', model.get('id'));
      });
    },

    cancel() {
      this.get('model').destroyRecord();
      history.back();
    }
  }
});
