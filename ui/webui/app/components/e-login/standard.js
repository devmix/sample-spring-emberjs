import Component from '@ember/component';

export default Component.extend({

  classNames: ['e-login'],

  session: Ember.inject.service('session'),

  user: 'admin',
  password: 'admin',

  actions: {
    authenticate() {
      const {user, password, session} = this.getProperties('user', 'password', 'session');
      session.authenticate('authenticator:oauth2', user, password).catch((reason) => {
        this.set('errorMessage', reason ? Ember.String.htmlSafe(reason.error || reason) : 'fail');
      });
    }
  }
});
