import DS from 'ember-data';
import ENV from 'webui/config/environment';
import SecuredAdapterMixin from 'ember-simple-auth/mixins/data-adapter-mixin';

export default DS.RESTAdapter.extend(SecuredAdapterMixin, {

  namespace: 'api/core/entity',
  host: ENV.host,
  authorizer: 'authorizer:oauth2',

  pathForType: function (modelName) {
    const hypen = modelName.indexOf('-');
    if (hypen !== -1) {
      const scope = modelName.substr(0, hypen),
        type = modelName.substr(hypen + 1);
      return `${scope}/${type}`;
    }
    return this._super(modelName);
  },

  /**
   * @override
   */
  ajax() {
    const response = this._super(...arguments);
    response.catch((e) => {
      if (e.message && e.message.indexOf('aborted') !== -1) {
        this.get('session').invalidate();
      }
    });
    return response;
  }
});
