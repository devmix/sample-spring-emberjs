import Service from '@ember/service';
import ENV from 'webui/config/environment';

export default Service.extend({

  session: Ember.inject.service(),

  createForEntities(options) {
    return this._ajax('POST', {
      url: ENV.APP.entityApiUrl + 'export/create',
      dataType: 'json',
      data: JSON.stringify(options),
      contentType: 'application/json; charset=UTF-8'
    });
    // window.open(url, '_blank');
  },

  _optionsToQueryParams(options) {
    if (!options) {
      return;
    }

    const query = [];
    for (let param in options) {
      if (options.hasOwnProperty(param)) {
        const value = options[param];
        if (param) {
          query.push(`${param} = ${value}`);
        }
      }
    }

    return query.length > 0 ? encodeURI(query.join('&')) : undefined;
  },

  _ajax(method, request) {
    return new Ember.RSVP.Promise((resolve, reject) => {
      Ember.$.ajax({
        type: method,
        url: request.url,
        data: request.data,
        dataType: request.dataType,
        contentType: request.contentType,
        processData: request.processData,
        headers: {
          'Authorization': 'Bearer ' + this.get('session.data.authenticated.access_token')
        }
      }).then(function (data) {
        Ember.run(null, resolve, data);
      }, function (jqXHR) {
        jqXHR.then = null; // tame jQuery's ill mannered promises
        Ember.run(null, reject, arguments);
      });
    });
  },
});
