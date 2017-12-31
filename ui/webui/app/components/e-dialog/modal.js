import UiModal from 'semantic-ui-ember/components/ui-modal';
import AutoRegisterMixin from 'webui/mixins/components/autoregister';

export default UiModal.extend(AutoRegisterMixin, {

  classNameBindings: ['fullscreen:fullscreen', 'size'],

  class: undefined,
  title: undefined,
  message: undefined,
  autoClose: true,
  fullscreen: false,
  size: undefined, // mini, tiny, small, large
  buttons: [
    {text: 'Accept', class: 'primary', action: 'e-dialog-modal-accept'},
    {text: 'Cancel', action: 'e-dialog-modal-cancel'},
  ],

  _visible: false,
  _title: undefined,
  _btnAccept: undefined,
  _btnCancel: undefined,
  _data: undefined,

  show(options = {}) {
    this.setProperties({
      _title: options.title || this.get('title'),
      _message: options.message || this.get('message'),
      _buttons: options.buttons || this.get('buttons'),
      _data: options.data
    });

    this.set('_visible', true);
    this.execute('show');
  },

  hide() {
    this.execute('hide');
    this.set('_visible', false);
  },

  invokeAction(name) {
    this.sendAction('action', name, this.get('_data'));

    if (this.get('autoClose')) {
      this.hide();
    }
  },

  actions: {
    onButtonAction: function (action) {
      this.invokeAction(action);
    }
  }
});
