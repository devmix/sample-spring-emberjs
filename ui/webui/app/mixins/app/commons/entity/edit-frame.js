import Ember from 'ember';
import AutoRegisterMixin from 'webui/mixins/components/autoregister'

export default Ember.Mixin.create(AutoRegisterMixin, {

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
    registerFields(fields, register) {
      this.set('fields', register ? fields : undefined);
    }
  }

});
