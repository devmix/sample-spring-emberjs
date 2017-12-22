import Ember from 'ember';

export default Ember.Controller.extend({
  fields: undefined,

  actions: {
    registerFields(field, register) {
      this.set('fields', register ? field : undefined);
    },

    commit() {
      this.get('fields').commit().then(() => {
        this.transitionToRoute('storage.books.book');
      });
    },

    cancel() {
      this.get('fields').cancel().then(() => history.back());
    }
  }
});
