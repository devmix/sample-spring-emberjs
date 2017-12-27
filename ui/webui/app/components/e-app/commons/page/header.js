import Ember from 'ember';

export default Ember.Component.extend({
  tagName: '',

  session: Ember.inject.service('session'),

  authorizationAction: Ember.computed('session.isAuthenticated', function () {
    return this.get('session.isAuthenticated') ? 'Logout' : 'Login';
  }),

  actions: {
    toggleAuth() {
      const session = this.get('session');
      if (session.get('isAuthenticated')) {
        this.get('session').invalidate();
      } else {
        this.transitionToRoute('login');
      }
    }
  }
});
