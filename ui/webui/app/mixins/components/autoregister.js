import Ember from 'ember';

export default Ember.Mixin.create({

  didInsertElement() {
    this._super(...arguments);
    const action = this.get('register');
    if (action) {
      this.sendAction('register', this)
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const action = this.get('unregister');
    if (action) {
      this.sendAction('unregister', this)
    }
  }

});
