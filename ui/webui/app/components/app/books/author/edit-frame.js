import Ember from 'ember';
import AutoRegisterMixin from '../../../../mixins/components/autoregister'

export default Ember.Component.extend(AutoRegisterMixin, {

  tagName: '',

  params: undefined,

  onDialogAction(action) {

    switch (action) {
      case 'accept':
        return this.get('fields').commit();

      case 'cancel':
        return this.get('fields').cancel();
    }

    return Promise.reject();
  },

  actions: {
    registerFields(fields) {
      this.set('fields', fields);
    },
    unregisterFields(fields) {
      this.set('fields', undefined);
    }
  }

});
