import Component from '@ember/component';
import AutoRegisterMixin from '../../mixins/components/autoregister';

export default Component.extend(AutoRegisterMixin, {
  classNames: ['e-tabsheet-tab'],
  classNameBindings: ['active:active']
});
