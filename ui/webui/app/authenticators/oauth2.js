import OAuth2PasswordGrant from 'ember-simple-auth/authenticators/oauth2-password-grant';
import ENV from 'webui/config/environment';

const CLIENT_ID = 'webClient:3e80f2ab-701b-413a-8f9b-a86bccc4a6fd';

export default OAuth2PasswordGrant.extend({

  serverTokenEndpoint: ENV.host + '/oauth/token',

  _clientIdHeader: Ember.computed('clientId', function () {
    const base64ClientId = window.base64.encode(CLIENT_ID);
    return {Authorization: `Basic ${base64ClientId}`};
  })
});
