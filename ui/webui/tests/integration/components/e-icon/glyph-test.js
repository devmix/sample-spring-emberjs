import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-icon/glyph', 'Integration | Component | e icon/glyph', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-icon/glyph}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-icon/glyph}}
      template block text
    {{/e-icon/glyph}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
