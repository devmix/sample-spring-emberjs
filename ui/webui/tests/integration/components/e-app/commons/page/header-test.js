import {moduleForComponent, test} from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-app/commons/page/header', 'Integration | Component | e app/commons/page/header', {
  integration: true
});

test('it renders', function (assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-app/commons/page/header}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-app/commons/page/header}}
      template block text
    {{/e-app/commons/page/header}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
