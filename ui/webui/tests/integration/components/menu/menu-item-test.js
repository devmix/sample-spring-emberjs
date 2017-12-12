import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('menu/menu-item', 'Integration | Component | menu/menu item', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{menu/menu-item}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#menu/menu-item}}
      template block text
    {{/menu/menu-item}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
